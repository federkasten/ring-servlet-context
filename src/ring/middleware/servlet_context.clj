(ns ring.middleware.servlet-context)

(defn wrap-servlet-context
  "Removes the servlet context path from requested URI"
  [handler]
  (fn [request]
    (handler
     (if-let [servlet-req (:servlet-request request)]
       (let [context (.getContextPath ^javax.servlet.http.HttpServletRequest servlet-req)
             uri ^String (:uri request)]
         (if (.startsWith uri context)
           (assoc request :uri
                  (.substring uri (.length context)))
           request))
       request))))
