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

(defn point [p {:keys [on-drag]}]
  (let [r (re-frame/subscribe [:color-editor-point-radius])
        y-scale (re-frame/subscribe [:color-editor-y-scale])]
    (fn [p {:keys [on-drag]}]
      [:circle
       {:on-mouse-down #(dragging on-drag)
        :r @r
        :cx (x p)
        :cy (* @y-scale (y p))}])))

(defn segment [from to]
  (let [y-scale (re-frame/subscribe [:color-editor-y-scale])]
    (fn [from to]
      [:line
       {:x1 (x from) :y1 (* @y-scale (y from))
        :x2 (x to) :y2 (* @y-scale (y to))}])))

(defn move-point [bcr y-scale class k]
  (fn [x y]
    (let [new-point (g/point (- x (:left bcr)) (/ (- y (:top bcr)) y-scale))]
      (utils/debounce :move-point-dispatch 1
                      #(re-frame/dispatch [:color-editor-move-point class k new-point])))))

(defn path [bcr y-scale class points]
  [:g {:class class}
   [:g
    (for [[[k1 p1] [k2 p2]] (partition 2 1 points)] ^{:key (str "s" k1)}
      [segment p1 p2])]
   [:g
    (for [[k p] points] ^{:key (str "p" k)}
      [point p {:on-drag (move-point bcr y-scale class k)}])]])

(defn paths []
  (let [paths-data (re-frame/subscribe [:color-editor-paths])
        bcr (re-frame/subscribe [:color-editor-bcr])
        y-scale (re-frame/subscribe [:color-editor-y-scale])]
    (fn []
      (let [bcr @bcr
            y-scale @y-scale]
        (when bcr
          [:g
           (for [[class points] @paths-data] ^{:key class}
             [path bcr y-scale class points])])))))


(defn onresize-callback []
  (re-frame/dispatch [:set-color-editor-bcr]))

(defn svg []
  (reagent/create-class
   {:component-did-mount
    #(do (.addEventListener js/window "resize" onresize-callback)
         (onresize-callback))

    :reagent-render
    (fn []
      [:svg
       [paths]])}))

(defn color-editor [js-points selected-node]
  [:div#color-editor.color-editor
   [:div.svg-wrapper
    [svg]]])
