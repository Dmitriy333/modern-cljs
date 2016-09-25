(ns modern-cljs.views
  (:use [hiccup core page]))

(defn index-page []
  (html5
    [:head
     [:title "Hello World"]
     (include-css "/css/styles.css")
     (include-js "/js/modern-dev/goog/base.js")
     (include-js "/js/modern.js")
     ]
    [:body
     [:h1 "Hello World"]]))