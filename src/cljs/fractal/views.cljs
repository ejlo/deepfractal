(ns fractal.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]))

(defn home-panel []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div.content.home (str "Hello from " @name ". This is the Home Page!")])))

(defn about-panel []
  (fn []
    [:div.content.about "This is the About Page."]))

(defn onresize-callback []
  (let [div-elem (.getElementById js/document "fractal-canvas-div")
        canvas-elem (.getElementById js/document "fractal-canvas")
        width (.-clientWidth div-elem)
        height (.-clientHeight div-elem)]
    (re-frame/dispatch [:change-canvas-size {:width width :height height}])))

(defn fractal-canvas []
  (let [canvas-size (re-frame/subscribe [:canvas-size])]
    (reagent/create-class
     {:component-did-mount
      #(do (.addEventListener js/window "resize" onresize-callback)
           (onresize-callback))

      :component-will-unmount
      #(.removeEventListener js/window "resize" onresize-callback)

      :display-name "fractal-canvas-component"

      :reagent-render
      (fn []
        (let [canvas-size (re-frame/subscribe [:canvas-size])]
          (fn []
            [:div#fractal-canvas-div.fractal-canvas-div
             [:canvas#fractal-canvas.fractal-canvas @canvas-size]])))})))

(defn fractal-panel []
  (fn []
    [:div.content.fractal
     [:div.fractal-control-bar]
     [fractal-canvas]]))

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :fractal-panel [] [fractal-panel])
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
