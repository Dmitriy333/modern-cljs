(ns modern-cljs.service.browse-news-service
  (:use
    :require
    [modern-cljs.repository.newsrepository]
    [modern-cljs.repository.commentsrepository]
    [ring.util.response :as response])
  (:require [modern-cljs.model.model :as model])
  (:import (java.util Date)))

(def browseNewsPageState (atom {:news nil :comments nil}))

(defn init-page-state [newsId]
  (swap! browseNewsPageState assoc
         :news (find-by-id news-repo-component newsId)
         :comments (find-by-news-id CommentRepositoryComponent newsId)))

(defn add-comment [request]
  (let [commentItem (:params request)]
    (let [comment (model/->Comment nil (:text commentItem) (new Date) (:newsId commentItem) (:userId commentItem))]
      (add CommentRepositoryComponent comment))
    (response/redirect (str "/news/" (:newsId commentItem)))))

(defn getBrowseNewsPageState [] @browseNewsPageState)