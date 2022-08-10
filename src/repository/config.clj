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

(defn- b64-decode
    [target]
    (String. (.decode (java.util.Base64/getDecoder) target)))

(defn get-api-url [] (get-env "API_URL" "/api/intel"))

(defn get-db-host [] (get-env "DB_HOST"))

(defn get-db-port [] (Integer/parseInt (get-env "DB_PORT")))

(defn get-db-username [] (get-env "DB_USERNAME"))

(defn secret-db-password [] (get-env "DB_PASSWORD"))
