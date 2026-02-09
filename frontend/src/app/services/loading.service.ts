import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {

  private loadingSubject = new BehaviorSubject<boolean>(false);
  private loadingMessageSubject = new BehaviorSubject<string>('Carregando...');

  isLoading$ = this.loadingSubject.asObservable();
  loadingMessage$ = this.loadingMessageSubject.asObservable();

  show(message?: string) {
    if (message) {
      this.loadingMessageSubject.next(message);
    }
    this.loadingSubject.next(true);
  }

  hide(){
    this.loadingSubject.next(false);
  }

  setMessage(message: string) {
    this.loadingMessageSubject.next(message);
  }
}
