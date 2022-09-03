(defproject repository "0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
                  [org.clojure/clojure              "1.10.1"]
                  [cambium/cambium.core             "1.1.0"]
                  [cambium/cambium.codec-cheshire   "1.0.0"]
                  [cambium/cambium.logback.json     "0.4.4"]
                  [slingshot                        "0.12.2"]
                  [ring/ring-core                   "1.9.4"]
                  [ring/ring-jetty-adapter          "1.9.4"]
                  [compojure                        "1.6.2"]
                  [de.ubercode.clostache/clostache  "1.4.0"]
                  [org.clojure/data.json            "2.4.0"]
                  [org.clojure/java.jdbc            "0.7.12"]
                  [org.postgresql/postgresql        "42.3.6"]
                  [middlesphere/clj-compress        "0.1.0"]
                ]
  :main ^:skip-aot repository.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
)