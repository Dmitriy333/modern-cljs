(ns modern-cljs.service.newsservice
  (:use
    :require
    [modern-cljs.repository.newsrepository]
    [modern-cljs.repository.commentsrepository]
    [modern-cljs.repository.logrepository]
    [ring.util.response :as response])
  (:require [modern-cljs.model.model :as model]
            [modern-cljs.repository.crudrepository :as crudRepository]
            [modern-cljs.repository.newsrepository :as newsRepository]
            [modern-cljs.repository.auditrepository :as auditRepository]
            [modern-cljs.service.userservice :as userservice]
            [modern-cljs.repository.logrepository :as logRepository]
            [modern-cljs.service.logservice :as logService]
            [clojure.data.json :as json])
  (:import (java.util Date)
           (java.sql Timestamp)))

(def browseNewsPageState (atom {:news nil :comments nil}))

(defn my-value-writer [key value]
  (if (= key :creation_date)
    (str (Timestamp. (.getTime value)))
    value))

(defn getNewsView [request]
  (if (userservice/isUserLoggedIn? request)
    (let [audit (model/->Audit nil (get-in request [:params :id]) (userservice/getAuthenticatedUserEmail request) (new Date))]
      (crudRepository/add auditRepository/auditRepositoryComponent audit)))
  {
   :news     (crudRepository/find-by-id newsRepository/newsRepositoryComponent (get-in request [:params :id]))
   :comments (find-by-news-id commentRepositoryComponent (get-in request [:params :id]))
   })

(defn init-page-state [newsId]
  (swap! browseNewsPageState assoc
         :news (crudRepository/find-by-id newsRepository/newsRepositoryComponent newsId)
         :comments (find-by-news-id commentRepositoryComponent newsId)))

(defn add-comment [request]
  (let [commentItem (:params request)]
    (let [comment (model/->Comment nil (:text commentItem) (new Date) (:newsId commentItem) (:userId commentItem))]
      (crudRepository/add commentRepositoryComponent comment))
    (response/redirect (str "/news/" (:newsId commentItem)))))

(defn remove-comment [request]
  (let [commentId (:commentId (:params request))]
    (let [comment (crudRepository/find-by-id commentRepositoryComponent commentId)]
      ;(logService/log-event comment "delete-comment")
      )
    (crudRepository/delete commentRepositoryComponent commentId))
  (response/redirect (str "/news/" (:newsId (:params request)))))


(defn getBrowseNewsPageState [] @browseNewsPageState)