import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  private documentClickTargetSubject: Subject<HTMLElement> = new Subject<HTMLElement>();
  public documentClickTarget = this.documentClickTargetSubject.asObservable();

  constructor() { 

  }
  
  setDocumentClickTarget(element: HTMLElement){
    this.documentClickTargetSubject.next(element);
  }
}
