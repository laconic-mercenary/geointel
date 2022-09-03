(ns repository.storage
    (:require   [clojure.spec.alpha :as s]
                [cambium.core :as log]
                [clojure.java.jdbc :as sql]
                [repository.compress :as compress]
                [repository.config :as config]))

(defn- get-db-spec [] (config/get-db-url))

(defn- decompress-img
    [row]
    (let [result (compress/decompress-bytes (:img row))]
        (if (:ok result)
            (assoc row :img (:decompressed result))
            (throw (new Exception (format "failed to decompress img with creation %s and note '%s'" (:created row) (:note row)) ))
        )
    )
)

(defn get-all
    []
    (log/trace "get-all")
    (try 
        { :ok true :entries (sql/query  (get-db-spec) 
                                        ["select img, latitude, longitude, accuracy, note, created from repository"] 
                                        {:row-fn decompress-img}) }
        (catch Exception e 
            (log/error (format "%s" (.getMessage e)))
            { :ok false :error "failed to query intel" }
        )
    )
)

(defn store
    [img lon lat accr note]
    (log/trace "store")
    (let [result (compress/compress-str img)]
        (if-not (:ok result)
            (do 
                (log/error "failed to compress image - will not store intel") 
                result)
            (try 
                (sql/insert! (get-db-spec) :repository { :latitude   lat 
                                                         :longitude  lon
                                                         :accuracy   accr
                                                         :note       note
                                                         :img        (:compressed result) })
                { :ok true }
                (catch Exception e 
                    (log/error (format "%s" (.getMessage e)))
                    { :ok false :error "failed to store intel" }))
        )
    )
)