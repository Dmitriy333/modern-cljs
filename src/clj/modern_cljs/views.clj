(ns modern-cljs.views
  (:use [hiccup core page]
        [hiccup.form :as f]
        [hiccup.element :only (link-to)]
        :require [modern-cljs.repository.newsrepository :as newsrepo]
        [modern-cljs.model.model :as model])
  (:import (java.text SimpleDateFormat)))

(defn nav-bar []
  (html5 [:nav {:class "navbar navbar-default navbar-fixed-top"}
          [:div {:class "container"}
           [:div {:class "navbar-header"}
            [:div (link-to {:class "navbar-brand"} (str "/news") "News Portal")]]
           [:div {:id "navbar" :class "navbar-collapse collapse"}
            [:ul {:class "nav navbar-nav"}
             [:li (link-to "/add-news" "Add News")]]
            ]]]))

(defn comments-component []
  (html5
    ;[:div {:ng-controller "MainCtrl"}
    ; [:h1 "Comments Component"]
    ; [:button {:onClick "addComment(1)"} "remove comment button"]
    ; ]

    (f/form-to {:enctype "application/json"} [:post "/api/add-news"]
               (f/hidden-field :user-id 42)
               (f/hidden-field :newsId 1)
               (f/text-field :title)
               (f/text-field :content)
               (submit-button {:class "btn" :name "submit"} "Save")
               )
    )
  )

(defn news-list []
  (html5
    [:head
     [:title "News Page"]
     (include-css "/css/base.css" "/css/main-news-page.css")
     (include-css "/css/bootstrap.min.css")
     (include-js "/providers/jquery-3.1.1.min.js")
     (include-js "/code/mainjscode.js")
     (include-js "/js/dev/goog/base.js")
     (include-js "/js/modern.js")
     ]
    [:body
     (nav-bar)
     [:div {:class "container news-container"}
      (comments-component)
      (let [news-list (find-all news-repo-component)]
        (for [news news-list]
          [:div {:class "news-item"}
           [:div {class "news-title"} (link-to (str "/news/" (:id news)) (:title news))]
           [:div {class "news-short-text"} (:short-text news)]
           [:div (link-to {:class "btn btn-primary"} (str "/news/" (:id news)) "Read")]
           [:hr]]))]
     ]))


(defn browse-news [id]
  (let [news (find-by-id news-repo-component id)]
    (html5
      [:head
       [:title (str "Read News" (:title news))  ]
       (include-css "/css/base.css")
       (include-css "/css/bootstrap.min.css")
       ]
      [:body
       (nav-bar)
       [:div {:class "container news-container"}
        ;(comments-component)
        [:div (:title news)]
        [:div (.format (SimpleDateFormat. "yyyy-MM-dd") (:creation-date news))]
        [:div (:full-text news)]
        ]
       ])))

(defn quick-form [& [name message error]]
  (html
    (form-to {:enctype "application/json"}
      [:post "/news"]
      (text-field "Hello")
      (submit-button {:class "btn" :name "submit"} "Save"))))

(defn add-news-page []
  (html5
    [:head
     [:title "Read News"]
     (include-css "/css/base.css")
     (include-css "/css/bootstrap.min.css")
     (include-js "/js/dev/goog/base.js")
     (include-js "/js/modern.js")
     ]
    [:body
     (nav-bar)
     [:div {:class "container news-container"}]
     [:input {:type "text-box"}]
     (quick-form)
     ]))

