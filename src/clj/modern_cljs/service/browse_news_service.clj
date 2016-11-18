(ns modern-cljs.service.browse-news-service
  (:use
    :require
    [modern-cljs.repository.newsrepository]
    [modern-cljs.repository.commentsrepository]))

(def browseNewsPageState (atom {:news nil :comments nil}))

(defn init-page-state [browseNewsPageState newsId]
  (swap! browseNewsPageState assoc
         :news (find-by-id news-repo-component newsId)
         :comments (find-by-news-id CommentRepositoryComponent newsId)))