(ns modern-cljs.service.browse-news-service
  (:use
    :require
    [modern-cljs.repository.newsrepository]
    [modern-cljs.repository.commentsrepository]
    [modern-cljs.repository.logrepository]
    [ring.util.response :as response])
  (:require [modern-cljs.model.model :as model]
            [modern-cljs.repository.crudrepository :as crudRepository]
            [modern-cljs.repository.newsrepository :as newsRepository]
            [modern-cljs.repository.logrepository :as logRepository]
            [clojure.data.json :as json])
  (:import (java.util Date)
           (java.sql Timestamp)))

(def browseNewsPageState (atom {:news nil :comments nil}))

(defn my-value-writer [key value]
  (if (= key :creation_date)
    (str (Timestamp. (.getTime value)))
    value))


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
    (crudRepository/add logRepositoryComponent (model/->Log nil (json/write-str (crudRepository/find-by-id commentRepositoryComponent commentId) :value-fn my-value-writer) "comment" (new Date)))
    ;(crudRepository/add logRepositoryComponent (model/->Log nil (json/write-str (new Date)) "comment" (new Date)))
    (crudRepository/delete commentRepositoryComponent commentId))

  (response/redirect (str "/news/" (:newsId (:params request)))))


(defn getBrowseNewsPageState [] @browseNewsPageState)