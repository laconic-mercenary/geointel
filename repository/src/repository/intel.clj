(ns repository.intel
    (:require   [clojure.spec.alpha :as s]
                [repository.storage :as storage]
                [cambium.core :as log]))

(defn add-intel
    [img lon lat note]
    (log/info "adding intel...FOR REAL") 
    (storage/store))