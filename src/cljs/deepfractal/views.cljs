(ns deepfractal.views
  (:require [re-frame.core :as re-frame]
            [deepfractal.views.panels :as panels]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div.wrapper
       (panels/menu @active-panel)
       (panels/panels @active-panel)])))
