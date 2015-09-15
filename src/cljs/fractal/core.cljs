(ns fractal.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [fractal.handlers]
              [fractal.subs]
              [fractal.views :as views]))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [] 
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
