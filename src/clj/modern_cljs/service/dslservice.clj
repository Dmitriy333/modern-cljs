(ns modern-cljs.service.dslservice
  (:require [ring.util.response :as response]
            [modern-cljs.views.views :as views]
            [modern-cljs.views.layout :as layout]
            [modern-cljs.dsl.dslparser :as dslp]))

(defn add-dsl [request]
  (let [rule (get-in request [:params :rule])]
    (dslp/parse-rule rule)

    (response/redirect "/dsl")
    ))
