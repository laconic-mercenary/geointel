(ns repository.routes
  (:require [compojure.core :as compojure]
            [compojure.route :as compojure-route]
            [repository.handlers :as handlers]))

(compojure/defroutes app
  (compojure/GET "/" params handlers/index)
  (compojure/GET "/api" params handlers/api-health)
  (compojure/GET "/api/intel" params handlers/get-intel)
  (compojure/POST "/api/intel" params handlers/add-intel)
  (compojure-route/not-found handlers/not-found))