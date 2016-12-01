(ns modern-cljs.dsl.dsltaskexecutor
  (:require
    [modern-cljs.dsl.dsltask :as t]))

(defn extract-param [param]
  (if (.equals (class param) String)
    (do (str "\""param"\""))
    param) )

(defn execute-task [dsl-task entity email details]
  (binding [*ns* (:ns (meta #'t/send-string-details-by-email-task))]
    (eval (read-string (str "("
                            dsl-task " '"
                            (extract-param entity) " '"
                            (extract-param email) " '"
                            (extract-param details)
                            ")"  )))
    ))

