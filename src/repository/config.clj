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

(defn get-svr-port [] (Integer/parseInt (get-env "PORT" "8080")))

(defn get-svr-max-threads [] (Integer/parseInt (get-env "SVR_MAX_THREADS" "30")))

(defn get-svr-thread-idle-timeout [] (Integer/parseInt (get-env "SVR_THREAD_IDLE_TIMEOUT" "10000")))

(defn get-svr-max-idle-time [] (Integer/parseInt (get-env "SVR_MAX_IDLE_TIME" "8000")))

(defn get-api-url [] (get-env "API_URL" "/api/intel"))

(defn get-db-url [] (get-env "DATABASE_URL"))

(defn get-db-host [] (get-env "DB_HOST"))

(defn get-db-port [] (Integer/parseInt (get-env "DB_PORT")))

(defn get-db-username [] (get-env "DB_USERNAME"))

(defn secret-db-password [] (get-env "DB_PASSWORD"))
