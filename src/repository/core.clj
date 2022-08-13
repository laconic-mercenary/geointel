(ns repository.core
  (:gen-class)
  (:require
      [cambium.core :as log]
      [repository.config :as config]
      [ring.adapter.jetty :as jetty]
      [compojure.core :as compojure]
      [compojure.route :as compojure-route]
      [repository.routes :as routes]))

(defn -main
  "Server for geointel"
  [& args]
  (log/info "starting...") 
  (jetty/run-jetty routes/app 
                  { :port                 (config/get-svr-port)
                    :join?                false 
                    :max-threads          (config/get-svr-max-threads)
                    :thread-idle-timeout  (config/get-svr-thread-idle-timeout)
                    :max-idle-time        (config/get-svr-max-idle-time)
                    :max-queued-requests  20    }))
