(ns repository.handlers
    (:require   [repository.config :as config]
                [repository.intel :as intel]
                [clostache.parser :as mustache]
                [clojure.spec.alpha :as s]
                [cambium.core :as log]
                [ring.util.request :as ring]
                [clojure.data.json :as json]))

(s/def ::img string?)

(defn index
  [request]
  (mustache/render-resource "templates/scout.html"
                            {
                                :api-url (config/get-api-url)
                            }))

(defn api-health
  [request]
  (log/info "health check") 
  { :status   200
    :headers  {  "Content-Type" "text/html"  }
    :body     "OK"  })

(defn add-intel
  [request]
  (log/info "adding intel...") 
  (let [body (ring/body-string request)]
    (let [decoded-body (json/read-str body)]
      (intel/add-intel  (:image decoded-body ) 
                        (:note decoded-body ) 
                        (:longitude (:location decoded-body )) 
                        (:latitude (:location decoded-body )))))
  { :status   200
    :headers  {  "Content-Type" "text/html"  }
    :body     "OK"  })

(defn not-found
    [request]
    { :status   404
      :headers  {  "Content-Type" "text/html"  }
      :body     "not found"  })
