(ns modern-cljs.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]
        [hiccup.form]
        [hiccup.element :only (link-to)]
        ))

;(ns modern-cljs.views.layout
;  (:use [hiccup core page]
;        [hiccup.form]
;        [hiccup.element :only (link-to)]
;        :require [modern-cljs.repository.newsrepository :as newsrepo]
;        [modern-cljs.model.model :as model])
;  (:import (java.text SimpleDateFormat)))

(defn nav-bar []
  (html5 [:nav {:class "navbar navbar-default navbar-fixed-top"}
          [:div {:class "container"}
           [:div {:class "navbar-header"}
            [:div (link-to {:class "navbar-brand"} (str "/news") "News Portal")]]
           [:div {:id "navbar" :class "navbar-collapse collapse"}
            [:ul {:class "nav navbar-nav"}
             [:li (link-to "/add-news" "Add News")]]
            ]]]))

(defn application [title & content]
  (html5 {:ng-app "newsApp" :lang "en"}
         [:head
          [:title title]
          (include-css "/css/bootstrap.min.css")
          (include-js "http://code.angularjs.org/1.2.3/angular.min.js")
          (include-js "/js/angular-1.2.3-min.js")

          [:body
           (nav-bar)
           [:div {:class "container"} content]
           ]
          ]))