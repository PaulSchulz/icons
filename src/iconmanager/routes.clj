(ns iconmanager.routes
  (:use compojure.core
        iconmanager.views
        iconmanager.view-icons
        iconmanager.view-about
        [hiccup.middleware :only (wrap-base-url)]
        ring.middleware.refresh)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response])
  ;; Copied from 'fontmanager' project
  (:import (java.util.prefs Preferences)
           (java.lang System)
           (java.awt GraphicsEnvironment))
  )

(def options
  {:dir-incoming "~/Documents/git/monomatch-myriad/icons/incoming"
   :dir-icons    "~/Documents/git/monomatch-myriad/icons/svg"
   :dir-archive  "~/Documents/git/monomatch-myriad/icons/archive"
   :dir-metadata "resources/public/icons/metadata"
   })

;; The user preferences for this application.
(def pref-node (.node (Preferences/userRoot) "iconmanager"))

(defroutes main-routes
  (GET "/"           []     (index-page options))
  (GET "/icons"      []     (icons-page options))
  (GET "/icon/:hash" [hash] (icon-page options hash))
  (GET "/tags"       []     (tags-page))
  (GET "/about"      []     (about-page options))
  ;; New pages
  (GET "/icondir" [] (.set pref-node "icon-dir" ""))
  (GET "/homedir" [] (System/getProperty "user.home"))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)
      ;; Monitor file and automatically refresh them
      (wrap-refresh)
      ))
