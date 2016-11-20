(ns modern-cljs.views.views
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
     [:input {:type "hidden" :name "newsId" :value (:id (:news (getBrowseNewsPageState)))}]
     [:input {:type "hidden" :name "userId" :value "1"}]
     [:input {:type "text" :name "text" :required "true"}]
     [:input {:type "submit" :value "Add comment" :class "btn btn-primary"}]
     ]))

(defn news-list-view []
  (html5
    [:div {:class "container news-container"}
     (let [news-list (find-all newsRepositoryComponent)]
       (for [news news-list]
         [:div {:class "news-item"}
          [:div {class "news-title"} (link-to (str "/news/" (:id news)) (:title news))]
          [:div {class "news-short-text"} (:short_text news)]
          [:div (link-to {:class "btn btn-success"} (str "/news/" (:id news)) "Read")]
          [:hr]]))
    ]))

(defn browse-news-view [id]
  (init-page-state id)
  (let [news (:news (getBrowseNewsPageState))]
     [:div {:class "container news-container"}
      [:div (:title news)]
      [:div (.format (SimpleDateFormat. "yyyy-MM-dd") (:creation_date news))]
      [:div (:full_text news)]
      (comments-component (:comments (getBrowseNewsPageState)))
      ]))

(defn add-news-view []
   (html5
     [:form {:enctype "application/json" :method "post" :action "/api/add-news" :class "container news-container"}
      [:span "Title:"] [:input {:type "text" :name "title" :class "news-title" :required "true"}]
      [:br]
      [:span "Short text:"] [:textarea {:name "shortText" :class "news-short-text" :required "true"}]
      [:br]
      [:span "Text: "] [:textarea {:name "fullText" :class "text" :required "true"}]
      [:br]
      [:input {:type "submit" :value "Add news" :class "btn btn-primary"}]
      ]))

(defn registration-view []
  (html5
    [:form {:enctype "application/json" :method "post" :action "/api/register" :class "container news-container" :onsubmit "return submitRegistrationForm()"}
     [:span "Email"] [:input {:type "email" :id "email" :name "email" :required "true"}]
     [:br]
     [:span "Name"] [:input {:type "text" :id "name" :name "login" :required "true"}]
     [:br]
     [:span "Password"] [:input {:type "password" :id "password" :name "password" :required "true"}]
     [:br]
     [:span "Confirm Password"] [:input {:type "password" :id "confirm-password" :name "confirmedPassword" :required "true"}]
     [:br]
     [:input {:type "submit" :value "Register" :class "btn btn-success"}]]))

