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
(def status-unfound 404)

(defn- rx-json
  [status body]
  { 
    :status   status
    :headers  { "Content-Type" "application/json" }
    :body     body
  })

(defn- rx-text
  [status body]
  { 
    :status   status
    :headers  { "Content-Type" "text/html" }
    :body     body
  })

(defn index
  [request]
  (mustache/render-resource "templates/scout.html"
                            {
                                :api-url (config/get-api-url)
                            }
  )
)

(defn api-health
  [request]
  (log/info "health-check")
  (rx-text status-ok "ok")
)

(defn get-intel
  [request] 
  (log/debug "get-intel")
  (let [result  (intel/get-intel)]
    (if-not (:ok result)
      (rx-text status-err "server error")
      (rx-json status-ok (json/write-str (:entries result)))
    )
  )
)

(defn add-intel
  [request] 
  (log/debug "add-intel")
  (let [body (ring/body-string request)]
    (let [decoded-body (json/read-str body :key-fn keyword)]
      (let [result (intel/add-intel (:image decoded-body) 
                                    (:note decoded-body) 
                                    (:longitude (:location decoded-body)) 
                                    (:latitude (:location decoded-body))
                                    (:accuracy (:location decoded-body)))]
        (if-not (:ok result)
          (if (contains? :param-error result)
            (rx-text status-param (:param-error result))
            (rx-text status-err "server error")
          )
          (rx-text status-ok "add-intel-ok")
        )
      )
    )
  )
)

(defn not-found
    [request]
    (rx-text status-unfound "not found"))
