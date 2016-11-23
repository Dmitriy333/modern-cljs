(ns modern-cljs.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]
        [hiccup.form]
        [hiccup.element :only (link-to)]
        [modern-cljs.service.userservice :as userservice]
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

(defn authenticated-user-nav-bar []
  (html5
    (navbar-add-news-template)
    (navbar-logout-template)))

(defn non-authenticated-user-nav-bar []
  (html5
    (navbar-login-template)
    (navbar-registration-template)))


(defn nav-bar [request]
  (html5 [:nav {:class "navbar navbar-default navbar-fixed-top"}
          [:div {:class "container"}
           [:div {:class "navbar-header"}
            [:div (link-to {:class "navbar-brand"} "/" "News Portal")]]
           [:div {:id "navbar" :class "navbar-collapse collapse"}
            [:ul {:class "nav navbar-nav"}
              (if (userservice/isUserLoggedIn? request)
                (authenticated-user-nav-bar)
                (non-authenticated-user-nav-bar))


             ;(navbar-login-template)


                   ;(navbar-registration-template)
             ;(navbar-logout-template)

             ]
            ]]]))

(defn application [title request & content]
  (println request)
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
           (nav-bar request)
           [:div {:class "container"} content]
           ]
          ]))