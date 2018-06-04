(ns ring.middleware.servlet-context
  (:import javax.servlet.http.HttpServletRequest))

(defn- update-uri [{:keys [servlet-request ^String uri] :as request}]
  (if servlet-request
    (let [context (.getContextPath ^HttpServletRequest servlet-request)]
      (if (.startsWith uri context)
        (assoc request :uri (.substring uri (.length context)))
        request))
    request))

(defn wrap-servlet-context
  "Removes the servlet context path from requested URI"
  [handler]
  (fn servlet-context-wrapped-handler
    ([request]
     (handler (update-uri request)))
    ([request respond raise]
     (handler (update-uri request) respond raise))))
