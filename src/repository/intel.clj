(ns repository.intel
    (:require   [clojure.spec.alpha :as s]
                [repository.storage :as storage]
                [repository.config :as config]
                [cambium.core :as log]))

(defn- valid-lon? [lon] (and (<= lon 180.0) (>= lon -180.0)))
(defn- valid-lat? [lat] (and (<= lat 90.0) (>= lat -90.0)))
(defn- valid-note? [note] (and (<= (count note) 750) (>= (count note) 1)))
(defn- valid-image? [img] (and (< (count img) 750000) (> (count img) 100)))
(defn- valid-accuracy? [accr] (and (<= accr 5000) (>= accr 0)))

(s/def ::longitude valid-lon?)
(s/def ::latitude valid-lat?)
(s/def ::note valid-note?)
(s/def ::image valid-image?)
(s/def ::accuracy valid-accuracy?)

(defn authorized?
    [key]
    (= key (config/secret-intel-key)))

(defn get-intel 
    []
    (storage/get-all))

(defn add-intel
    [img note lon lat accr]
    (log/trace "add-intel")
    (log/debug "validating intel ...")
    (if-not (s/valid? ::longitude   lon)    {:ok false :param-error "invalid longitude"})
    (if-not (s/valid? ::latitude    lat)    {:ok false :param-error "invalid latitude"})
    (if-not (s/valid? ::accuracy    accr)   {:ok false :param-error "invalid accuracy"})
    (if-not (s/valid? ::note        note)   {:ok false :param-error "invalid note"})
    (if-not (s/valid? ::image       img)    {:ok false :param-error "invalid image"})
    (log/info (format "intel: coords [%f,%f], note (%s), img (%s..., %d chars)" lon lat note (subs img 0 100) (count img)))
    (storage/store img lon lat accr note)
)
