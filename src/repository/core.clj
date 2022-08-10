(ns repository.core
  (:gen-class)
  (:require
      [cambium.core :as log]
      [ring.adapter.jetty :as jetty]
      [compojure.core :as compojure]
      [compojure.route :as compojure-route]
      [repository.routes :as routes]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (log/info "starting...") 
  (jetty/run-jetty routes/app 
                  { :port                 8080
                    :join?                false 
                    :max-threads          30 
                    :thread-idle-timeout  10000  
                    :max-idle-time        8000  
                    :max-queued-requests  20    }))
