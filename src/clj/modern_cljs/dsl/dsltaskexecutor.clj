(ns modern-cljs.dsl.dsltaskexecutor
  (:require
    [modern-cljs.dsl.dsltask :as t]))

(defn extract-param [param]
  (if (.equals (class param) String)
    (do (str "\""param"\""))
    param) )

(defn execute-task [dsl-task & params]
  (binding [*ns* (:ns (meta #'t/send-string-details-by-email-task))]
    (let [code (for [param params]
                 (str " '" (extract-param param)))]
      (let [resulted-code (str "(" dsl-task (clojure.string/join code) ")" )]
        (eval (load-string resulted-code))))))

