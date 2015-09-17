(ns fractal.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [fractal.handlers]
            [fractal.subs]
            [fractal.routes :as routes]
            [fractal.views :as views]
            [fractal.math.mandel :as mandel]
            ))

(enable-console-print!)

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
