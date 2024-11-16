package com.smallworldfs.moneytransferapp.modules.home.presentation.presentation.implementation;

/**
 * Created by luis on 13/6/17
 * <p>
 * <p>
 * THIS CLASS CAN BE FULLY DELETED IF IT'S NO NEED TO MIGRATE ACTIVITY TO COMPOSE
 */

/**

 THIS CLASS CAN BE FULLY DELETED IF IT'S NO NEED TO MIGRATE ACTIVITY TO COMPOSE

 */

/*
@Singleton
public class HomePresenterImpl extends GenericPresenterImpl implements HomePresenter {

    private final HomePresenter.View mView;
    private final long MILLIS_IN_A_DAY = 86400000;

    public UserDataRepository userDataRepository;
    public UserDataLocalDatasource userDataLocalDatasource;
    public UserMapperFromDTO userMapperFromDTO;
    public LocaleRepository localeRepository;
    public UserResponseMapper userResponseMapper;
    public UserDataNetworkDatasource userDataNetworkDatasource;
    public UserDTOMapper userDTOMapper;
    public APIErrorMapper apiErrorMapper;

    @Inject
    public HomePresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, GenericActivity activity, View view) {
        super(observeOn, context);
        this.mActivity = activity;
        this.mView = view;

        DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        DaggerHiltEntryPoint.class);
        userDataLocalDatasource = hiltEntryPoint.provideUserDataLocalDatasource();
        userMapperFromDTO = hiltEntryPoint.provideUserMapperFromDTO();
        localeRepository = hiltEntryPoint.provideLocaleRepository();
        userResponseMapper = hiltEntryPoint.provideUserResponseMapper();
        userDataNetworkDatasource = hiltEntryPoint.provideUserDataNetworkDataSource();
        userDTOMapper = hiltEntryPoint.provideUserDTOMapper();
        apiErrorMapper = hiltEntryPoint.provideAPIErrorMapper();

        userDataRepository = new LegacyUserDataRepositoryImpl(
                userDataLocalDatasource,
                userMapperFromDTO,
                localeRepository,
                userResponseMapper,
                userDataNetworkDatasource,
                apiErrorMapper,
                userDTOMapper
        );
    }

    @Override
    public void create() {
        super.create();

        mView.configureView();

        //Request delivery methods

        //Request payout countries
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            Pair<String, String> fromCountry = new Pair<>(Objects.requireNonNull(user.getCountry().firstEntry()).getKey(), Objects.requireNonNull(user.getCountry().firstEntry()).getValue());

            // Crashlytics event
            FirebaseCrashlytics.getInstance().setCustomKey("Country", fromCountry.first);

            // Track id of ContentSquare if is not limited (limited user has not accepted privacy terms and conditions)
            if (user.getStatus().equals(Constants.UserType.LIMITED)) {
                Contentsquare.optOut(mContext);
            } else {
                Contentsquare.optIn(mContext);
            }
            (new SMSConsentRunner()).execute(user);

        }

        // Check if should show gdpr popup
        /**
         * COMMENTED FOR COMPOSE TO COMPILE
         */
        /*Gdpr gdprInfo = mInteractor.getGdprInfo();
        if (gdprInfo != null && gdprInfo.getListGdprMessages() != null && gdprInfo.getListGdprMessages().size() > 0) {
            mView.showGdprBlockingPopup(
                    gdprInfo.getTitle(),
                    gdprInfo.getListGdprMessages(),
                    Constants.QUICK_REMINDER_STYLES.GDPR,
                    gdprInfo.getType(),
                    gdprInfo.getButtonOkTitle(),
                    gdprInfo.getButtonCancelTitle()
            );
        }
    }

    @Override
    public void resume() {
        super.resume();
        updateTabIndicator();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void destroy() {
        CalculatorInteractorImpl.getInstance().destroy();
        super.destroy();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    public void checkPrimeForPushDailyEvent() {
        long lastEventTimestamp = userDataLocalDatasource.getLastPrimeForPushEventTimestamp();
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp >= lastEventTimestamp + MILLIS_IN_A_DAY) {
            mView.registerBrazeEvent(BrazeEventName.PRIME_FOR_PUSH.getValue());
            userDataLocalDatasource.setLastPrimeForPushEventTimestamp(currentTimestamp);
        }
    }

    private void updateTabIndicator() {
        int selectedPosition = mView.getSelectedTabPosition();
        if (selectedPosition == 0) {
            mView.hideTabIndicator(0);
        } else if (selectedPosition == 1) {
            mView.hideTabIndicator(1);
        }
        final int documentRequirements = DocumentsRepository.getInstance().getDocumentRequirements();
        if (documentRequirements > 0) {
            mView.showTabIndicator(2, documentRequirements);
        }
    }

    public void checkEmailValidated() {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(userDataRepository
                .getUseStatusLegacy()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        user -> {
                            if (!user.getEmailValidated())
                                mView.showSendEmailValidationDialog(user.getEmail());
                            else {
                                mView.showAppRatingDialog();
                            }
                        },
                        exception -> {
                            Log.e("TRANSACTION_ERROR", exception.getMessage());
                        }
                )
        );
    }

    public void showEmailValidated() {
        User user = userDataRepository.retrieveUser();
        if (user.getShowEmailValidated())
            mView.showEmailValidatedDialog();

    }

    public void setShowEmailValidated(boolean showEmailValidated) {
        User user = userDataRepository.retrieveUser();
        user.setShowEmailValidated(showEmailValidated);
        userDataRepository.putUser(user);
    }

    public User getUser() {
        return userDataRepository.retrieveUser();
    }

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    public interface DaggerHiltEntryPoint {
        UserDataLocalDatasource provideUserDataLocalDatasource();

        UserMapperFromDTO provideUserMapperFromDTO();

        LocaleRepository provideLocaleRepository();

        UserResponseMapper provideUserResponseMapper();

        UserDataNetworkDatasource provideUserDataNetworkDataSource();

        UserDTOMapper provideUserDTOMapper();

        APIErrorMapper provideAPIErrorMapper();
    }
}

 */
