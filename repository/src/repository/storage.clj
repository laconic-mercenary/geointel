(ns repository.storage
    (:require   [clojure.spec.alpha :as s]
                [cambium.core :as log]
                [clojure.java.jdbc :as sql]
                [repository.config :as config]))

(def repo-query "select img, latitude, longitude, note from repository")

(defn- get-db-spec []
    {
        :dbtype "postgresql"
        :dbname "geointel"
        :host (config/get-db-host)
        :port (config/get-db-port)
        :user (config/get-db-username)
        :password (config/secret-db-password)
    }
)

(defn get-all
    []
    (log/debug "get-all")
    (try 
        { :ok true :entries (sql/query (get-db-spec) repo-query) }
        (catch Exception e 
            (log/error (format "%s" (.getMessage e)))
            { :ok false :error "failed to query intel" }
        )
    )
)

(defn store
    [img lon lat note]
    (log/debug "store")
    (try 
        (sql/insert! (get-db-spec) :repository { :latitude   lat 
                                                 :longitude  lon
                                                 :note       note
                                                 :img        img })
        { :ok true }
        (catch Exception e 
            (log/error (format "%s" (.getMessage e)))
            { :ok false :error "failed to store intel" }
        )
    )
)