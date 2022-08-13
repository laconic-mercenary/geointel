(ns repository.compress-test
  (:require [clojure.test :refer :all]
            [repository.compress :refer :all]))

(deftest symmetrical-compression
  (testing "Verifies that we can compress a string into bytes and return it to it's original string form"
    (is 
      (let [test-str "asifoasfjasd')(#&%#&JMMEMDSD=D0FDF  sdf "]
        (let [compressed (compress-str test-str)]
          (print (count compressed))
          (= (decompress-bytes compressed) test-str)
        )
      )
    )
  )
)
