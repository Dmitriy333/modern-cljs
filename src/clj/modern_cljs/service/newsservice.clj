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

(def watchedNewsAtom (atom '()))                            ;create atom with empty list
(def auditorAgent (agent nil))

(defn audit-function [agentstate audits]
  (println "batch inserting started ...")
  (println "adding " audits)
  (auditRepository/insert-multiple-audits auditRepository/auditRepositoryComponent audits)
  (println "batch inserting ended ..."))

(add-watch watchedNewsAtom :watcher
   (fn [key atom old-state new-state]
      (if (= 2 (count new-state))
        (do
          (send auditorAgent audit-function new-state)
          ;(let [agentSender (agent new-state)]
          ;  (send auditorAgent audit-function new-state))
          ;;invoke agent action here and pass a list of news
          (reset! watchedNewsAtom '())))))

(defn auditWatchedNewsInMemory [audit]
  (let [watchedNewsAuditList @watchedNewsAtom]
    (reset! watchedNewsAtom (conj watchedNewsAuditList audit))))

(defn getNewsView [request]
  (if (userservice/isUserLoggedIn? request)
    (let [audit (model/->Audit nil (get-in request [:params :id]) (userservice/getAuthenticatedUserEmail request) (new Date))]
    (auditWatchedNewsInMemory audit)))
   {
   :news     (crudRepository/find-by-id newsRepository/newsRepositoryComponent (get-in request [:params :id]))
   :comments (find-by-news-id commentRepositoryComponent (get-in request [:params :id]))
   })

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