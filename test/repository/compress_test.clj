(ns repository.compress-test
  (:require [clojure.test :refer :all]
            [repository.compress :refer :all]))

(deftest symmetrical-compression
  (testing "Verifies that we can compress a string into bytes and return it to it's original string form"
    (is 
      (let [  test-str "asifoasfjasd')(#&%#&JMMEMDSD=D0FDF  sdf "
              comp-result (compress-str test-str)
              decomp-result (decompress-bytes (:compressed comp-result))  ]
        (= test-str (:decompressed decomp-result))
      )
    )
  )
)
