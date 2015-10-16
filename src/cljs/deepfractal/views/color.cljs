(ns deepfractal.views.color
  (:require [goog.events :as events]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [deepfractal.utils :as utils]
            [deepfractal.math.point :as g :refer [x y dist]])
  (:import [goog.events EventType]))


(defn drag-start [drag-move drag-end on-start drag-end-atom]
  (on-start)
  (reset! drag-end-atom drag-end)
  (events/listen js/window EventType.MOUSEMOVE drag-move)
  (events/listen js/window EventType.MOUSEUP drag-end))

(defn drag-move-fn [on-drag]
  (fn [evt]
    (on-drag (.-clientX evt) (.-clientY evt))))

(defn drag-end-fn [drag-move drag-end-atom on-end]
  (fn [evt]
    (events/unlisten js/window EventType.MOUSEMOVE drag-move)
    (events/unlisten js/window EventType.MOUSEUP @drag-end-atom)
    (on-end)))


(defn dragging
  ([on-drag] (dragging on-drag (fn []) (fn [])))
  ([on-drag on-start on-end]
   (let [drag-move (drag-move-fn on-drag)
         drag-end-atom (atom nil)
         drag-end (drag-end-fn drag-move drag-end-atom on-end)]
     (drag-start drag-move drag-end on-start drag-end-atom))))

(defn point [{:keys [on-drag]} p]
  [:circle
   {:on-mouse-down #(dragging on-drag)
    :r 5
    :cx (x p)
    :cy (y p)}])

(defn segment [from to]
  [:line
   {:x1 (x from) :y1 (y from)
    :x2 (x to) :y2 (y to)}])

(defn get-bounding-rect [svg-root]
  (-> svg-root
      reagent/dom-node
      .getBoundingClientRect))

(defn move-point [svg-root class k]
  (fn [x y]
    (let [bcr (get-bounding-rect svg-root)
          new-point (g/point (- x (.-left bcr)) (- y (.-top bcr)))]
      (utils/debounce :move-point-dispatch 1
                      #(re-frame/dispatch [:color-editor-move-point class k new-point])))))

(defn path [svg-root class points]
  (prn "(path)" class points)
  [:g {:class class}
   [:g
    (for [[[k1 p1] [k2 p2]] (partition 2 1 points)] ^{:key (str "s" k1)}
      [segment p1 p2])]
   [:g
    (for [[k p] points] ^{:key (str "p" k)}
      [point {:on-drag (move-point svg-root class k)} p])]])

(defn paths [svg-root]
  (prn "(paths)")
  (let [paths-data (re-frame/subscribe [:color-editor-paths])]
    [:g
     (for [[class points] @paths-data] ^{:key class}

       [path svg-root class points])]))

(defn svg [{:keys [width height]}]
  [:svg
   {:width (or width 1700)
    :height (or height 300)
    :style {:border "1px solid black"}}
   [paths (reagent/current-component)]])

(defn color-editor [js-points selected-node]
  [:div#color-editor.color-editor
   [svg]])
