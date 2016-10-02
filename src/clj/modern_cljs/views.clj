(ns modern-cljs.views
  (:use [hiccup core page]
        [hiccup.form]
        [hiccup.element :only (link-to)]
        :require [modern-cljs.repository.newsrepository :as newsrepo]))

(defn news-list []
  (html5
    [:head
     [:title "News Page"]
     (include-css "/css/bootstrap.min.css")
     (include-js "/js/dev/goog/base.js")
     (include-js "/js/modern.js")
     ]
    [:body
     [:h1 "News list"]
     [:div {:class "news-list-container"}]
     (for [news (newsrepo/load-all-news)]
       [:div news
        (link-to {:class "btn btn-primary"} "/news/1" "Take me to Home")
        ])
     ]))

(defn browse-news [id]
  (let [news (newsrepo/get-news-by-id id)]
    (html5
      [:head
       [:title "Read News"]
       (include-css "/css/bootstrap.min.css")
       (include-js "/js/dev/goog/base.js")
       (include-js "/js/modern.js")
       ]
      [:body
       [:h1 "News list"]
       [:div {:class "news-container"}]
       (class news)
       ;;(apply hash-map news)
       news

       ;(get-in news :news_id)
       ;(get-in news :short_text)
       ;(get-in news :full_text)
       ;(get-in news :creation_date)
       ])
    )
  )