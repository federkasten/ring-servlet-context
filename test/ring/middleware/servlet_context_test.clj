(ns ring.middleware.servlet-context-test
  (:require [clojure.test :refer :all]
            [ring.util.response :as response]
            [ring.util.servlet :as servlet]
            [ring.adapter.jetty :as jetty]
            [clj-http.client :as http]
            [ring.middleware.servlet-context :as sc])
  (:import org.eclipse.jetty.server.Server
           [org.eclipse.jetty.servlet ServletContextHandler ServletHolder]))

(defn- wrap-servlet-handler [handler opts]
  (fn [^Server server]
    (->> "/*"
         (.addServlet (ServletHolder. (servlet/servlet handler opts)))
         (doto (ServletContextHandler.)
           (.setContextPath (:servlet-context-path opts)))
         (.setHandler server))))

(defn- get-resolved-uri [{:keys [async? port servlet-context-path] :as opts}]
  (let [handler (sc/wrap-servlet-context
                    (if async?
                      (fn [request respond raise]
                        (respond (response/response (:uri request))))
                      (fn [request]
                        (response/response (:uri request)))))
        server (jetty/run-jetty
                (when-not servlet-context-path handler)
                (assoc opts :configurator
                       (when servlet-context-path
                         (wrap-servlet-handler handler opts))))]
    (try
      (:body (http/get (str "http://localhost:" port "/foo/bar/baz")))
      (finally
        (.stop server)))))

(deftest root-sync-handler
  (is (get-resolved-uri {:async? false, :join? false, :port 54321})
      "/foo/bar/baz"))

(deftest root-async-handler
  (is (get-resolved-uri {:async? true, :join? false, :port 54322})
      "/foo/bar/baz"))

(deftest servlet-sync-handler
  (is (get-resolved-uri {:async? false, :join? false, :port 54323,
                         :servlet-context-path "/foo"})
      "/bar/baz"))

(deftest servlet-async-handler
  (is (get-resolved-uri {:async? true, :join? false, :port 54324,
                         :servlet-context-path "/foo"})
      "/bar/baz"))
