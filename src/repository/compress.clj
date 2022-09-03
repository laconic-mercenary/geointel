(ns repository.compress
    (:gen-class)
    (:require
        [cambium.core :as log]
        [clj-compress.core :as compression]
        [clojure.java.io :refer [file output-stream input-stream] :as io])
    (:import 
        [java.io ByteArrayOutputStream]))

(def zip-encoding "UTF-8")
(def compression-type "gz")

(defn compress-str
    [target-str]
    (log/trace "compress-str")
    (with-open [byte-out-stream  (ByteArrayOutputStream.)]
        (try
            (let [  bytes (.getBytes target-str zip-encoding)
                    cnt (compression/compress-data bytes byte-out-stream compression-type)
                    compressed-bytes (.toByteArray byte-out-stream) ]
                (log/debug (format "compression report - orig:%d, new:%d" (count bytes) (count compressed-bytes)))
                { :ok true :compressed compressed-bytes })
            (catch Exception e 
                (log/error (format "error: %s. string: %s..." (.getMessage e) (subs target-str 25)))
                (.printStackTrace e)
                { :ok false :error "failed to compress string" })
        )
    )
)

(defn decompress-bytes
    [bytes]
    (log/trace "decompress-bytes")
    (with-open [byte-out-stream  (ByteArrayOutputStream.)]
        (try 
            (let [  cnt (compression/decompress-data bytes byte-out-stream compression-type) ]
                (log/debug (format "decompression report - orig:%d, new:%d" (count bytes) cnt))
                { :ok true :decompressed (.toString byte-out-stream zip-encoding) })
            (catch Exception e 
                (log/error (format "error: %s. bytes-len: %d. " (.getMessage e) (count bytes)))
                (.printStackTrace e)
                { :ok false :error "failed to decompress bytes" }))
    )
)
