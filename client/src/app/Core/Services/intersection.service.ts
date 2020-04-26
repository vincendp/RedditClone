import { Injectable } from "@angular/core";

export interface LazyTarget {
  element: Element;
  emitLoadedElement: () => void;
}

@Injectable({
  providedIn: "root",
})
export class IntersectionService {
  intersectionObserver: IntersectionObserver;
  elementTargets: Map<Element, LazyTarget>;

  constructor() {
    this.intersectionObserver = new IntersectionObserver((entries) => {
      this.checkForIntersection(entries);
    }, {});
    this.elementTargets = new Map();
  }

  addTarget(target: LazyTarget) {
    this.elementTargets.set(target.element, target);
    this.intersectionObserver.observe(target.element);
  }

  removeTarget(target: LazyTarget) {
    this.elementTargets.delete(target.element);
    this.intersectionObserver.unobserve(target.element);
  }

  checkForIntersection = (entries: Array<IntersectionObserverEntry>) => {
    entries.forEach((entry: IntersectionObserverEntry) => {
      if (entry.isIntersecting) {
        let lazyTarget = this.elementTargets.get(entry.target);
        if (lazyTarget) {
          lazyTarget.emitLoadedElement();
        }
      }
    });
  };
}
