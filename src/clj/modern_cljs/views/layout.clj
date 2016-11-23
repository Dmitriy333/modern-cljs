(ns modern-cljs.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]
        [hiccup.form]
        [hiccup.element :only (link-to)]
        [modern-cljs.service.sessionservice :as sessionservice]
        ))

(defn navbar-registration-template []
  (html5
    [:li (link-to "/registration" "Registration")]))

(defn navbar-login-template []
  (html5
    [:li (link-to "/login" "Login")]))

(defn navbar-logout-template []
  (html5
    [:li (link-to "/logout" "Logout")]))


(defn navbar-add-news-template []
  (html5
    [:li (link-to "/add-news" "Add News")]))

(defn nav-bar []
  (html5 [:nav {:class "navbar navbar-default navbar-fixed-top"}
          [:div {:class "container"}
           [:div {:class "navbar-header"}
            [:div (link-to {:class "navbar-brand"} "/" "News Portal")]]
           [:div {:id "navbar" :class "navbar-collapse collapse"}
            [:ul {:class "nav navbar-nav"}

             (navbar-login-template)

                   ;(navbar-add-news-template)
                   ;(navbar-registration-template)
             ;(navbar-logout-template)

             ]
            ]]]))

(defn application [title & content]
  (html5
         [:head
          [:title title]
          (include-css "/css/bootstrap.min.css")
          (include-css "/css/base.css" "/css/main-news-page.css")
          (include-js "/js/dev/goog/base.js")
          (include-js "/js/modern.js")
          (include-js "/providers/jquery-3.1.1.min.js")
          (include-js "/code/main.js")
          [:body
           (nav-bar)
           [:div {:class "container"} content]
           ]
          ]))