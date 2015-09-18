(ns deepfractal.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [deepfractal.handlers]
            [deepfractal.subs]
            [deepfractal.routes :as routes]
            [deepfractal.views :as views]
            [deepfractal.math.mandel :as mandel]
            ))

(enable-console-print!)

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
