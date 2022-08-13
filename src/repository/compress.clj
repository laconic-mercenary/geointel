(ns repository.compress
    (:gen-class)
    (:require
        [cambium.core :as log]
        [clj-compress.core :as compression]
        [clojure.java.io :refer [file output-stream input-stream] :as io])
    (:import 
        [java.io ByteArrayOutputStream])
)

(def zip-encoding "UTF-8")
(def compression-type "gz")

(defn compress-str
    [str]
    (with-open [byte-out-stream  (ByteArrayOutputStream.)]
        (compression/compress-data (.getBytes str zip-encoding) byte-out-stream compression-type)
        (.toByteArray byte-out-stream)
    )
)

(defn decompress-bytes
    [bytes]
    (with-open [byte-out-stream  (ByteArrayOutputStream.)]
        (compression/decompress-data bytes byte-out-stream compression-type)
        (.toString byte-out-stream zip-encoding)
    )
)
