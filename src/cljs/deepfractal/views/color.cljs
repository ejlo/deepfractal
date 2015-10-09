(ns deepfractal.views.color)

(defn color-editor []
  [:div#color-editor.color-editor

   ]
  )

(defn mousedown []
  (let [node (-> js/d3 (.mouse (-> svg-selection (.node))))]
    (re-frame/dispatch [:color-svg-add-node node])
    (redraw!))
  )

(defn svg-selection []
  (-> js/d3
      (.select "#color-editor")))

(defn draw-editor []
  (let [width 960
        height 500]
    (-> (svg-selection)
        (.append "svg")
        (.attr "width" width)
        (.attr "height" height))
    (-> (svg-selection)
        (.append "rect")
        (.attr "width" width)
        (.attr "height" height)
        (.on "mousedown" mousedown))

    (-> (svg-selection)
        (.append "path")
        (.datum points)
        (.attr "width" width)
        (.attr "height" height)
        (.on "mousedown" mousedown))
    (-> js/d3
        (.select js/window)
        (.on "mousemove" mousemove)
        (.on "mouseup" mouseup)
        (.on "keydown" keydown))
    (-> svg-selection .node .focus)
    svg))

(defn draw-circles [points selected-node dragged-node]
  (let [line (-> js/d3 .-svg .line)
        path-selection (-> svg-selection
                           (.select "path")
                           (.attr "d" line))
        circle (-> svg-selection
                   (.selectAll "circle")
                   (.data points identity))]

    ;; new circle
    (-> circle
        (.enter)
        (.append "circle")
        (.attr "r" 1e-6)
        (.on "mousedown" #(re-frame/dispatch [:color-svg-select-node %]))
        (.transition)
        (.duration 750)
        (.ease "elastic")
        (.attr "r" 6.5))

    ;; changed (?) circles
    (-> circle
        (.classed "selected" #(= (.-id %) selected-node))
        (.attr "cx" #(.-x %))
        (.attr "cy" #(.-y %)))

    ;; removed circles
    (-> circle
        (.exit)
        (.remove))

    (when-let [ev (-> js/d3 -.event)]
      (-> ev (.preventDefault))
      (-> ev (.stopPropagation))))



;;   function redraw() {
;;   svg.select("path").attr("d", line);

;;   var circle = svg.selectAll("circle")
;;       .data(points, function(d) { return d; });

;;   circle.enter().append("circle")
;;       .attr("r", 1e-6)
;;       .on("mousedown", function(d) { selected = dragged = d; redraw(); })
;;     .transition()
;;       .duration(750)
;;       .ease("elastic")
;;       .attr("r", 6.5);

;;   circle
;;       .classed("selected", function(d) { return d === selected; })
;;       .attr("cx", function(d) { return d[0]; })
;;       .attr("cy", function(d) { return d[1]; });

;;   circle.exit().remove();

;;   if (d3.event) {
;;     d3.event.preventDefault();
;;     d3.event.stopPropagation();
;;   }
;; }

  )
