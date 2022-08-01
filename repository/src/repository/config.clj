(ns repository.config
    (:gen-class)
    (:require
        [cambium.core :as log]))

(defn- get-env
    ([key]
        (if-let [envval (System/getenv key)]
        envval
        (throw (new Exception (format "'%s' env var -  or -  system property is required" key)))))
    ([key dflt]
        (if-let [envval (System/getenv key)]
        envval
        dflt)))

(defn get-api-url
    []
    (get-env "API_URL" "/api/intel"))
