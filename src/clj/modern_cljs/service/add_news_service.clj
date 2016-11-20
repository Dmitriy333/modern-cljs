(ns modern-cljs.service.add-news-service
  (:use
    :require
    [modern-cljs.repository.newsrepository]
    [modern-cljs.repository.commentsrepository]
    [ring.util.response :as response])
  (:require [modern-cljs.model.model :as model]
            [modern-cljs.repository.crudrepository :as crudRepository])
  (:import (java.util Date)))

(defn add-news [request]
  (let [newsItem (:params request)]
    (crudRepository/add newsRepositoryComponent (model/->News nil (:title newsItem) (:shortText newsItem) (:fullText newsItem) (new Date))))
  (response/redirect "/"))