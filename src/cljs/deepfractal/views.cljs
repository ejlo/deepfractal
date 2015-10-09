(ns deepfractal.views
  (:require [re-frame.core :as re-frame]
            [deepfractal.views.fractal :as fractal-view]
            [deepfractal.utils :as utils]))

(defn home-panel []
  (fn []
    [:div.content.home "This is the Home Page!"]))

(defn about-panel []
  (fn []
    [:div.content.about "This is the About Page."]))

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :fractal-panel [] [fractal-view/fractal-panel])
(defmethod panels :default [] [:div])

(defn menu-item [item item-name url active-panel]
  [:li (when (= active-panel item) {:class "active"})
   [:a.noselect {:href url} item-name]]
  )

(defn menu [active-panel]
  [:nav.navbar.navbar-default>div.container-fluid
   [:div.navbar-header
    [:div.navbar-brand.noselect "DeepFractal"]]
   [:div.collapse.navbar-collapse
    [:ul.nav.navbar-nav
     [menu-item :home-panel "Home" "#/" active-panel]
     [menu-item :fractal-panel "Fractal" "#/fractal" active-panel]
     [menu-item :about-panel "About" "#/about" active-panel]]]])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div.wrapper
       (menu @active-panel)
       (panels @active-panel)])))
