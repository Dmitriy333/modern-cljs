(ns modern-cljs.service.browse-news-service
  (:use
    :require
    [modern-cljs.repository.newsrepository]
    [modern-cljs.repository.commentsrepository]
    [ring.util.response :as response])
  (:require [modern-cljs.model.model :as model]
            [modern-cljs.repository.crudrepository :as crudRepository]
            [modern-cljs.repository.newsrepository :as newsRepository])
  (:import (java.util Date)))

(def browseNewsPageState (atom {:news nil :comments nil}))

(defn init-page-state [newsId]
  (swap! browseNewsPageState assoc
         :news (crudRepository/find-by-id newsRepository/newsRepositoryComponent newsId)
         :comments (find-by-news-id commentRepositoryComponent newsId)))

(defn add-comment [request]
  (let [commentItem (:params request)]
    (let [comment (model/->Comment nil (:text commentItem) (new Date) (:newsId commentItem) (:userId commentItem))]
      (let [insertedId (:generated_key (first (crudRepository/add commentRepositoryComponent comment)))]
        (let [insertedComment (model/->Comment insertedId (:text commentItem) (new Date) (:newsId commentItem) (:userId commentItem))]
          (let [commentsList (:comments @browseNewsPageState)]
            (conj commentsList insertedComment)
            (swap! browseNewsPageState assoc :comments commentsList)))))
    (response/redirect (str "/news/" (:newsId commentItem)))))

(defn remove-comment [request]
  (let [commentId (:commentId (:params request))]
    (crudRepository/delete commentRepositoryComponent commentId))
  (response/redirect (str "/news/" (:newsId (:params request)))))

(defn getBrowseNewsPageState [] @browseNewsPageState)