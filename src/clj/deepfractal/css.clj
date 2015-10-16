(ns deepfractal.css
  (:require [garden.def :refer [defstyles]]
            [garden.units :as unit :refer [px em percent vh]]
            [garden.color :as color]))

(defn add-alpha [c a]
  (cond
   (color/color? c) (assoc c :alpha a)
   (color/hex? c) (add-alpha (color/as-rgb c) a)
   :default (add-alpha (color/from-name c) a)))

(defn rgba
  ([c a] (add-alpha c a))
  ([r g b a] (color/rgba r g b a)))


(def no-navbar-collapse
  [[:.navbar-collapse.collapse
    {:display :block!important
     :padding-bottom "0 !important"}]
   [:.container-fluid>.navbar-collapse
    :.container-fluid>.navbar-header
    :.container>.navbar-collapse
    :.container>.navbar-header
    {:margin-left "0 !important"
     :margin-right "0 !important"}]
   [:.navbar-header :.navbar-nav :.navbar-nav>li
    {:float :left!important}]
   [:.navbar-nav
    {:margin "0 !important"}]
   [:.navbar-nav>li>a
    {:padding-bottom "15px !important"
     :padding-top "15px !important"}]
   [:.navbar-nav.navbar-right:last-child
    {:margin-right "-15px"}]
   [:.navbar-right
    {:float :right!important}]
   [:.navbar-brand
    {:margin-left "-15px !important"}]])

(def navbar-style
  [:.navbar-default
   {:margin-bottom 0}
   [:.navbar-nav>li>a:hover
    {:background-color (rgba :black 0.01)}]
   [:.navbar-brand
    {:color "#32f"
     :font-weight :bold
     :background-color (rgba "#eec" 0.3)}]])

(def noselect
  [[:.noselect
     {:-webkit-touch-callout :none
      :-webkit-user-select :none
      :-khtml-user-select :none
      :-moz-user-select :none
      :-ms-user-select :none
      :user-select :none}]
   [:div.noselect
    {:cursor :default}]])

(def app
  [:#app
   {:position :absolute
    :top 0
    :left 0
    :height (percent 100)
    :width (percent 100)}
   [:.wrapper
    {:display :flex
     :flex-direction :column
     :flex-wrap :nowrap
     :height (percent 100)
     :width (percent 100)}
    [:.navbar]
    [:.content
     {:flex-grow 1
      :overflow :hidden}]]])

(def fractal-content
  (let [margin (em 0.2)
        border [[(px 1) :solid "#ddd"]]]
    [:.content.fractal
     {:position :relative
      :display :flex
      :flex-direction :column
      :flex-wrap :nowrap
      :margin margin}
     [:.fractal-column
      {:position :relative
       :flex-grow 4
       :display :flex
       :flex-direction :row
       :flex-wrap :nowrap
       :overflow :hidden
       }
      [:.fractal-control-bar
       {:min-width (em 10)
        :width (em 15)
        :margin margin
        :padding [[(em 0.2) (em 0.5)]]
        :border border}
       [:.input
        [:input
         {:width (percent 100)
          :padding [[(px 1) margin]]}]
        [:label
         {:margin 0
          :margin-top (em 1)}]
        [:&:first-child
         [:label
          {:margin-top (em 0)}]]]]
      [:.fractal-canvas-div
       {:flex-grow 1
        :border border
        :margin margin
        :overflow :hidden}]]
     [:.color-column
      {:min-height (vh 15)
       :flex-grow 1
       :margin margin
       :border border}]]))

(def color-svg
  [:svg
   [:circle
    {:fill "#fff"
     :fill-opacity 0.6
     :cursor :move
     :stroke :steelblue
     :stroke-width (px 1.5)}]

   [:line
    {:fill :none
     :stroke :black
     :stroke-width (px 2)}]

   [:.red   [:line {:stroke "#c00"}]]
   [:.green [:line {:stroke "#0c0"}]]
   [:.blue  [:line {:stroke "#00c"}]]
   [:.alpha [:line {:stroke "#888"}]]
])

(defstyles screen
  [noselect
   no-navbar-collapse
   navbar-style
   app
   fractal-content
   color-svg
   ])
