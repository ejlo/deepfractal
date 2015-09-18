(ns fractal.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [fractal.math.mandel :as mandel]
            [fractal.utils :as utils]))

(defn home-panel []
  (fn []
    [:div.content.home "This is the Home Page!"]))

(defn about-panel []
  (fn []
    [:div.content.about "This is the About Page."]))

(defn onresize-callback []
  (let [div-elem (.getElementById js/document "fractal-canvas-div")
        canvas-elem (.getElementById js/document "fractal-canvas")
        width (.-clientWidth div-elem)
        height (.-clientHeight div-elem)]
    (re-frame/dispatch [:set-canvas-size {:width width :height height}])))

(defn left-button? [event]
  (= (.-button event) 0))

(defn mousedown-callback [event]
  (let [i (.-offsetX event)
        j (.-offsetY event)]
    (when (left-button? event)
      (re-frame/dispatch [:canvas-zoom [i j] 1.3]))))

(defn fractal-canvas []
  (let [canvas-size (re-frame/subscribe [:canvas-size])
        fractal-params (re-frame/subscribe [:fractal-params])]
    (reagent/create-class
     {:component-did-mount
      #(do
         (.addEventListener js/window "resize" onresize-callback)
           (.addEventListener (.getDOMNode %) "mousedown" mousedown-callback)
           (onresize-callback))

      :component-will-unmount
      #(.removeEventListener js/window "resize" onresize-callback)

      :display-name "fractal-canvas-component"

      :reagent-render
      (fn []
        (utils/debounce "canvas" 250
                        #(mandel/draw-image @canvas-size @fractal-params))
        [:canvas#fractal-canvas.fractal-canvas @canvas-size
         {:data-params @fractal-params}])})))

(defn input [id label dispatch-event-key val {:keys [exponential?]}]
  (let [dispatch-fn #(re-frame/dispatch-sync [dispatch-event-key %])
        onchange-fn (fn [e]
                      (let [new-val (-> e .-target .-value)]
                        (dispatch-fn new-val)))]
    [:div.input
     [:label label]
     [:input {:type "number"
              :id id
              :value (if (and exponential? (number? val) (> val 10000))
                       (.toExponential val 3)
                       val)
              :on-change onchange-fn}]]))

(defn fractal-control-bar []
  (let [fractal-params (re-frame/subscribe [:fractal-params])]
    [:div.fractal-control-bar
     [:form
      [input "zoom" "Zoom" :set-zoom (:zoom @fractal-params) {:exponential? true}]
      [input "max-n" "Max Iterations" :set-max-n (:max-n @fractal-params)]
      [input "cx" "Center x" :set-center-x (first (:center @fractal-params))]
      [input "cy" "Center y" :set-center-y (second (:center @fractal-params))]
      ]]

    )
  )

(defn fractal-panel []
  (fn []
    [:div.content.fractal
     [fractal-control-bar]
     [:div#fractal-canvas-div.fractal-canvas-div
      [fractal-canvas]]]))

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
