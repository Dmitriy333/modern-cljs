(ns modern-cljs.views
  (:use [hiccup core page]
        [hiccup.form :as f]
        [hiccup.element :only (link-to)]
        :require
        [modern-cljs.repository.newsrepository :as newsrepo]
        [ring.util.response :as response]
        [modern-cljs.service.browse-news-service]
        [modern-cljs.model.model :as model]
        [modern-cljs.repository.crudrepository :as crudRepository])
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

(defn comments-component [comments]
  (html5
    (for [comment comments]
      [:form {:enctype "application/json" :method "post" :action "/api/delete-comment" :class "comment-item"}
       [:input {:type "hidden" :name "newsId" :value (:news_id comment)}]
       [:input {:type "hidden" :name "userId" :value (:user_id comment)}]
       [:input {:type "hidden" :name "commentId" :value (:id comment)}]
       [:div {class "comment-text"} (:text comment)]
       [:input {:type "submit" :value "Remove Comment" :class "btn btn-danger"}]
       [:hr]])

    [:form {:enctype "application/json" :method "post" :action "/api/add-comment"}
     [:input {:type "hidden" :name "newsId" :value "1"}]
     [:input {:type "hidden" :name "userId" :value "1"}]
     [:input {:type "text" :name "text" :required "true"}]
     [:input {:type "submit" :value "Add comment" :class "btn btn-primary"}]
     ]
    )
  )

(defn news-list []
  (html5
    [:head
     [:title "News Page"]
     [:meta {:charset "utf-8"}]
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
      (let [news-list (find-all newsRepositoryComponent)]
        (for [news news-list]
          [:div {:class "news-item"}
           [:div {class "news-title"} (link-to (str "/news/" (:id news)) (:title news))]
           [:div {class "news-short-text"} (:short_text news)]
           [:div (link-to {:class "btn btn-success"} (str "/news/" (:id news)) "Read")]
           [:hr]]))]
     ]))


(defn browse-news [id]
  (if (not (= id (:id (:news (getBrowseNewsPageState)))))
    (init-page-state id) (println "state was rerendered"))  ; not reload page state if it is not changed

  (let [news (:news (getBrowseNewsPageState))]
    (html5
      [:head
       [:meta {:charset "utf-8"}]
       [:title (str "Read News" (:title news))]
       (include-css "/css/base.css")
       (include-css "/css/bootstrap.min.css")
       ]
      [:body
       (nav-bar)
       [:div {:class "container news-container"}
        [:div (:title news)]
        [:div (.format (SimpleDateFormat. "yyyy-MM-dd") (:creation_date news))]
        [:div (:full_text news)]
        (comments-component (:comments (getBrowseNewsPageState)))
        ]
       ])))

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
     [ :form {:enctype "application/json" :method "post" :action "/api/add-news" :class "container news-container"}
      [:span "Title:"] [:input {:type "text" :name "title" :class "news-title" :required "true"}]
      [:br]
      [:span "Short text:"] [:textarea {:name "shortText" :class "news-short-text" :required "true"}]
      [:br]
      [:span "Text: "] [:textarea {:name "fullText" :class "text" :required "true"}]
      [:br]
      [:input {:type "submit" :value "Add news" :class "btn btn-primary"}]
      ]]))

