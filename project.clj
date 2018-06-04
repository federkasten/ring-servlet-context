(defproject ring-servlet-context "0.1.0"
  :description "Ring middleware for handling servlet context path"
  :url "https://github.com/federkasten/ring-servlet-context"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  ;; :aliases {"test-all" ["with-profile" "default:+1.6:+1.7:+1.8" "test"]}
  :profiles
  {:dev {:dependencies [[org.clojure/clojure "1.9.0"]
                        [javax.servlet/javax.servlet-api "4.0.1" :scope "provided"]]}
   :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
   :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
   :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :test {:dependencies [[ring/ring-jetty-adapter "1.6.3"]
                         [org.eclipse.jetty/jetty-servlet "9.2.24.v20180105"]
                         [ring/ring-servlet "1.6.3"]
                         [clj-http "3.9.0"]]}})
