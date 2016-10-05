(ns modern-cljs.service.newsservice
  (:require modern-cljs.repository.newsrepository
            :as
            newsrepo
            [modern-cljs.repository.newsrepository :as newsrepo]
            [modern-cljs.config.dbconfig :as dbconfig])
  (:import (modern_cljs.repository.newsrepository NewsRepository)))

;(defprotocol NewsServiceProtocol [newsrepository]
;  (get-news-list [newsrepository]
;    "news-list")
;
;  (add-news [newsrepository news]
;    news)
;
;  (delete-news [newsrepository news-id]
;    news-id))
;
;(deftype NewsService [(NewsRepository. dbconfig/mysql-db)]
;  NewsServiceProtocol
;  (get-news-list [newsrepository])
;  )
;
;(def NewsServiceProtocol. newsrepo)