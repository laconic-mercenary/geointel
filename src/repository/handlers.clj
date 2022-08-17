(ns repository.handlers
    (:require   [repository.config :as config]
                [repository.intel :as intel]
                [clostache.parser :as mustache]
                [clojure.spec.alpha :as s]
                [cambium.core :as log]
                [ring.util.request :as ring]
                [clojure.data.json :as json]))

(def status-ok 200)
(def status-err 500)
(def status-param 400)
(def status-unauthorized 401)
(def status-unfound 404)

(def content-json { "Content-Type" "application/json" })
(def content-text { "Content-Type" "text/html" })

(defn- rx-json
  [status body]
  { 
    :status   status
    :headers  content-json
    :body     body
  })

(defn- rx-text
  [status body]
  { 
    :status   status
    :headers  content-text
    :body     body
  })

(defn- persist-intel
  [decoded-body]
  (log/trace "persist-intel")
  (let [result (intel/add-intel (:image decoded-body)
                                (:note decoded-body)
                                (-> decoded-body :location :longitude)
                                (-> decoded-body :location :latitude)
                                (-> decoded-body :location :accuracy))]
    (if-not (:ok result)
      (if (contains? :param-error result)
        (rx-text status-param (:param-error result))
        (rx-text status-err "server error"))
      (rx-text status-ok "ok")
    )
  )
)

(defn- fetch-all-intel
  []
  (log/trace "fetch-all-intel")
  (let [result (intel/get-intel)]
    (if-not (:ok result)
      (rx-text status-err "server error")
      (rx-json status-ok (json/write-str (:entries result))))
  )
)

(defn- unauthorized-intel-key
  []
  (log/warn "intel api key was INCORRECT")
  (rx-text status-unauthorized "not authorized")
)

(defn- unspecified-intel-key
  []
  (log/warn "intel api key was not specified")
  (rx-text status-unauthorized "not authorized")
)

(defn- do-if-authorized
  [request handler]
  (log/trace "do-if-authorized")
  (let [body (ring/body-string request)
        decoded-body (json/read-str body :key-fn keyword)
        headers (:headers request)]
    (if (contains? :x-intel-api-key headers)
      (if (intel/authorized? (:x-intel-api-key headers))
        (handler decoded-body)
        (unauthorized-intel-key))
      (unspecified-intel-key))
  )
)

(defn index
  [request]
  (mustache/render-resource "templates/scout.html"
                            {
                                :api-url (config/get-api-url)
                                :api-key (config/secret-intel-key)
                            }
  )
)

(defn not-found
  [request]
  (log/warn "not-found")
  (rx-text status-unfound "not found")
)

(defn api-health
  [request]
  (log/info "health-check")
  (rx-text status-ok "ok")
)

(defn get-intel
  [request] 
  (log/trace "get-intel")
  (do-if-authorized request fetch-all-intel)
)

(defn add-intel
  [request]
  (log/trace "add-intel")
  (do-if-authorized request persist-intel)
)
