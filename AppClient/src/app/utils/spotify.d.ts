declare namespace Spotify {
    interface Player {
      connect(): Promise<boolean>;
      disconnect(): void;
      // Add more methods if necessary
    }
  
    interface PlayerOptions {
      name: string;
      getOAuthToken: (cb: (token: string) => void) => void;
    }
  
    class Player {
        [x: string]: any;
        constructor(options: PlayerOptions);
        connect(): Promise<boolean>;
        disconnect(): void;
      }
  }
  